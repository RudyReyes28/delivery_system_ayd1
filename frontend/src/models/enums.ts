export enum TipoDireccion {
  RESIDENCIA = 'RESIDENCIA',
  TRABAJO = 'TRABAJO',
  ENTREGA = 'ENTREGA',
  FISCAL = 'FISCAL'
}

// Función auxiliar para obtener las opciones del select
export function getTipoDireccionOptions() {
  return [
    { value: TipoDireccion.RESIDENCIA, label: 'Residencia' },
    { value: TipoDireccion.TRABAJO, label: 'Trabajo' },
    { value: TipoDireccion.ENTREGA, label: 'Entrega' },
    { value: TipoDireccion.FISCAL, label: 'Fiscal' }
  ];
}