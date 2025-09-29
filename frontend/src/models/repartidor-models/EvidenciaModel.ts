export interface EvidenciaModel {
  idEvidenciaEntrega: number;
  idGuia: number;
  tipoEvidencia: string;
  nombreArchivo: string;
  urlArchivo: string;
  nombreReceptor: string;
  documentoReceptor: string;
  parentescoReceptor: string;
  observaciones: string;
}

/**
 *  public enum TipoEvidencia {
        FOTO_PAQUETE,
        FOTO_ENTREGA,
        FIRMA_DIGITAL,
        DOCUMENTO_IDENTIDAD
    }
 */

export const TIPOS_EVIDENIA: string[] = [
  'FOTO_PAQUETE',
  'FOTO_ENTREGA',
  'FIRMA_DIGITAL',
  'DOCUMENTO_IDENTIDAD',
];
