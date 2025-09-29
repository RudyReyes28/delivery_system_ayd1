export interface ReporteCancelacionesEmpresa {
  idEmpresa: number;
  nombreEmpresa: string;
  totalCancelaciones: number;
  guiasCanceladas: GuiaCancelada[];
}

export interface GuiaCancelada {
  idGuia: number;
  numeroGuia: string;
  motivoCancelacion: string;
  fechaCancelacion: string;
  categoriaCancelacion: string;
}

export type ReporteCancelaciones = ReporteCancelacionesEmpresa[];
