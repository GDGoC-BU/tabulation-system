import { Link, createFileRoute } from '@tanstack/react-router'
import { useMemo } from 'react'
import Console from '@/components/console'
import Table from '@/components/table'
import { useAwardsQuery } from '@/features/awards/hooks/use-awards-query'
import { Button } from '@/components/ui/button'
import { awardTableColumns } from '@/features/awards/components/award-table-columns'
import { useSelectedPageantQuery } from '@/features/pageants/hooks/use-selected-pageant-query'
import { usePageantHierarchyQuery } from '@/features/pageants/hooks/use-pageant-hierarchy'
import renderFormulaLabel from '@/features/formula/lib/render-formula-label'

export const Route = createFileRoute('/admin/console/awards/')({
  component: AdminConsoleAwards,
})

function AdminConsoleAwards() {
  const { data: awards } = useAwardsQuery()
  const { data: selectedPageant } = useSelectedPageantQuery()
  const { data: pageantHierarchy } = usePageantHierarchyQuery(
    selectedPageant?.id,
  )

  const criterionMap = useMemo(() => {
    if (!pageantHierarchy) return new Map()
    const map = new Map<string, string>()
    pageantHierarchy.phases.forEach((phase) => {
      phase.segments.forEach((segment) => {
        segment.criteria.forEach((criterion) => {
          const key = criterion.id
          const value = `<${phase.name} : ${segment.name} : ${criterion.name}>`
          map.set(key, value)
        })
      })
    })

    return map
  }, [pageantHierarchy])

  /* Converts the formula to a more readable format just like in admin/console/awards/add */
  const processedAwards = useMemo(() => {
    if (!awards) return []
    return awards.map((award) => ({
      ...award,
      formula: renderFormulaLabel(award.formula, criterionMap),
    }))
  }, [awards, criterionMap])

  return (
    <Console>
      <Console.Header className="flex flex-row justify-between items-center">
        <Console.Header.Title>Awards</Console.Header.Title>
        <div className="">
          <Button asChild>
            <Link to="/admin/console/awards/add">Add Award</Link>
          </Button>
        </div>
      </Console.Header>
      <Console.Content>
        <Table.Data columns={awardTableColumns} data={processedAwards} />
      </Console.Content>
    </Console>
  )
}
