import {
  flexRender,
  getCoreRowModel,
  useReactTable,
} from '@tanstack/react-table'
import type { ColumnDef } from '@tanstack/react-table'
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from '@/components/ui/table'
import { cn } from '@/lib/utils'

type DataTableProps<TData, TValue> = {
  columns: Array<ColumnDef<TData, TValue>>
  data: Array<TData>
  limit: number
  formula: string
}

export default function Leaderboard<TData, TValue>({
  columns,
  data,
  limit,
  formula,
}: DataTableProps<TData, TValue>) {
  const table = useReactTable({
    data,
    columns,
    meta: {
      formula: formula,
    },
    getCoreRowModel: getCoreRowModel(),
  })

  return (
    <div className="overflow-hidden rounded-md border">
      <Table className="border-separate border-spacing-y-4 border-spacing-x-4">
        <TableHeader>
          {table.getHeaderGroups().map((headerGroup) => (
            <TableRow key={headerGroup.id}>
              {headerGroup.headers.map((header) => {
                return (
                  <TableHead key={header.id}>
                    {header.isPlaceholder
                      ? null
                      : flexRender(
                          header.column.columnDef.header,
                          header.getContext(),
                        )}
                  </TableHead>
                )
              })}
            </TableRow>
          ))}
        </TableHeader>
        <TableBody className="">
          {table.getRowModel().rows.length ? (
            table.getRowModel().rows.map((row, rowIndex) => {
              const ringClassName =
                rowIndex < limit ? 'ring-emerald-500' : 'ring-red-500'
              const borderClassName =
                rowIndex < limit ? 'border-emerald-500' : 'border-red-500'

              return (
                <TableRow
                  className={cn('ring-1 rounded-sm', ringClassName)}
                  key={row.id}
                  data-state={row.getIsSelected() && 'selected'}
                >
                  {row.getVisibleCells().map((cell, cellIndex) => {
                    return (
                      <TableCell
                        className={cn(
                          cellIndex < row.getVisibleCells().length - 1 &&
                            'border-r',
                          borderClassName,
                        )}
                        key={cell.id}
                      >
                        {flexRender(
                          cell.column.columnDef.cell,
                          cell.getContext(),
                        )}
                      </TableCell>
                    )
                  })}
                </TableRow>
              )
            })
          ) : (
            <TableRow>
              <TableCell colSpan={columns.length} className="h-24 text-center">
                No results.
              </TableCell>
            </TableRow>
          )}
        </TableBody>
      </Table>
    </div>
  )
}
