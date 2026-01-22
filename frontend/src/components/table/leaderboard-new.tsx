import {
  flexRender,
  getCoreRowModel,
  useReactTable,
} from '@tanstack/react-table'
import type { ColumnDef } from '@tanstack/react-table'
import type { LeaderboardEntrySummary } from '@/features/leaderboard/schemas'
import type { Formula } from '@/features/formula/schemas'
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from '@/components/ui/table'
import { cn } from '@/lib/utils'

type DataTableProps = {
  columns: Array<ColumnDef<LeaderboardEntrySummary>>
  data: Array<LeaderboardEntrySummary>
  formula: Formula
}

export default function LeaderboardNew({
  columns,
  data,
  formula,
}: DataTableProps) {
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
            table.getRowModel().rows.map((row) => {
              const isSelected: LeaderboardEntrySummary['isSelected'] =
                row.original.isSelected

              /* Top and bottom border on rows */
              const ringClassName = isSelected
                ? 'ring-emerald-500'
                : 'ring-red-500'
              /* Column separator on rows */
              const borderClassName = isSelected
                ? 'border-emerald-500'
                : 'border-red-500'

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
                        <div>{row.getValue('rank')}</div>
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
