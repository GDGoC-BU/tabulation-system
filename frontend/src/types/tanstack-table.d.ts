import '@tanstack/react-table'

declare module '@tanstack/react-table' {
  interface TableMeta<TData extends RowData> {
    formula?: string | null
  }

  interface ColumnMeta<TData extends RowData, TValue> {
    className?: string
  }
}
