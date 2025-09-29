export interface DescuentoNivel {
  idNivelFidelizacion: number;
  nombreNivel: string;
  codigoNivel: string;
  porcentajeDescuento: number;
  totalDescuentosAplicados: number;
  totalEmpresasBeneficiadas: number;
}

export type ReporteDescuentos = DescuentoNivel[];
