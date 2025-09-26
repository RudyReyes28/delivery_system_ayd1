import { ContratoComisionDTO, ContratoModel } from "./ContratoModel";

export interface ContratoItem {
  contrato: ContratoModel;
  comision: ContratoComisionDTO;
  expanded: boolean;
}
