import { Injectable } from '@angular/core';
import jsPDF from 'jspdf';
import html2canvas from 'html2canvas';

@Injectable({
  providedIn: 'root'
})
export class PdfExportService {
  constructor() { }

  /**
   * Exporta el contenido del elemento HTML a PDF
   * @param elementId ID del elemento HTML a exportar
   * @param fileName Nombre del archivo PDF a generar
   * @param pageTitle Título para el PDF
   */
  exportToPDF(elementId: string, fileName: string, pageTitle: string): void {
    // Obtener el elemento que queremos exportar
    const element = document.getElementById(elementId);
    if (!element) {
      console.error(`Elemento con ID ${elementId} no encontrado`);
      return;
    }

    // Configuración para html2canvas
    const options = {
      scale: 2, // Escala para mejorar la calidad
      useCORS: true, // Para permitir imágenes de otros dominios
      logging: false, // Desactivar logs
      scrollY: -window.scrollY, // Para manejar elementos fuera de la vista
      scrollX: 0
    };

    html2canvas(element, options).then(canvas => {
      // Tamaño del PDF en formato A4
      const imgWidth = 208; // Ancho en mm (A4 = 210mm, dejamos margen)
      const imgHeight = (canvas.height * imgWidth) / canvas.width;

      // Convertir el canvas a una imagen
      const imgData = canvas.toDataURL('image/png');
      
      // Crear el PDF
      const pdf = new jsPDF('p', 'mm', 'a4');
      
      // Añadir título al PDF
      pdf.setFontSize(16);
      pdf.text(pageTitle, 10, 10);
      
      // Añadir fecha de generación
      pdf.setFontSize(10);
      const today = new Date();
      pdf.text(`Generado el: ${today.toLocaleString('es-GT')}`, 10, 20);
      
      // Añadir la imagen del contenido
      pdf.addImage(imgData, 'PNG', 1, 25, imgWidth, imgHeight);
      
      // Guardar el PDF
      pdf.save(`${fileName}.pdf`);
    });
  }

  /**
   * Exporta múltiples elementos HTML a un solo PDF
   * @param elements Array de objetos con ID de elementos y sus títulos
   * @param fileName Nombre del archivo PDF a generar
   * @param documentTitle Título principal del documento
   */
  async exportMultipleElementsToPDF(
    elements: { elementId: string, title: string }[],
    fileName: string,
    documentTitle: string
  ): Promise<void> {
    const pdf = new jsPDF('p', 'mm', 'a4');
    const imgWidth = 208; // Ancho en mm (A4 = 210mm, dejamos margen)
    let currentPage = 1;
    let currentHeight = 25; // Posición Y inicial para contenido (después del título)

    // Añadir título principal y fecha
    pdf.setFontSize(16);
    pdf.text(documentTitle, 10, 10);
    
    pdf.setFontSize(10);
    const today = new Date();
    pdf.text(`Generado el: ${today.toLocaleString('es-GT')}`, 10, 20);
    
    // Procesar cada elemento
    for (let i = 0; i < elements.length; i++) {
      const element = document.getElementById(elements[i].elementId);
      if (!element) continue;
      
      // Capturar el elemento como imagen
      const canvas = await html2canvas(element, {
        scale: 2,
        useCORS: true,
        logging: false
      });
      
      const imgHeight = (canvas.height * imgWidth) / canvas.width;
      const imgData = canvas.toDataURL('image/png');
      
      // Si el contenido no cabe en la página actual, crear una nueva
      if (currentHeight + imgHeight > 280) { // 280mm es aproximadamente el alto útil de A4
        pdf.addPage();
        currentPage++;
        currentHeight = 10; // Reiniciar altura en nueva página
        
        // Añadir número de página
        pdf.setFontSize(8);
        pdf.text(`Página ${currentPage}`, imgWidth - 10, 290, { align: 'right' });
      }
      
      // Añadir título del elemento
      pdf.setFontSize(14);
      pdf.text(elements[i].title, 10, currentHeight);
      currentHeight += 8;
      
      // Añadir la imagen del elemento
      pdf.addImage(imgData, 'PNG', 1, currentHeight, imgWidth, imgHeight);
      currentHeight += imgHeight + 15; // Añadir espacio después de la imagen
    }
    
    // Añadir número de página en la primera página
    pdf.setPage(1);
    pdf.setFontSize(8);
    pdf.text(`Página 1 de ${currentPage}`, imgWidth - 10, 290, { align: 'right' });
    
    // Guardar el PDF
    pdf.save(`${fileName}.pdf`);
  }
}
