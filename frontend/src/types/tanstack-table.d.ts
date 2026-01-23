import type { Formula } from '@/features/formula/schemas'
import '@tanstack/react-table'

declare module '@tanstack/react-table' {
  interface TableMeta<TData extends RowData> {
    formula?: Formula
  }

  interface ColumnMeta<TData extends RowData, TValue> {
    className?: string
  }
}
