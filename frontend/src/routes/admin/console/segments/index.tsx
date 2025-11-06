import { createFileRoute } from '@tanstack/react-router'
import { useMemo } from 'react'
import { useSegmentsQuery } from '@/features/segments/hooks/use-segments-query'
import Console from '@/components/console'
import { segmentTableColumns } from '@/features/segments/components/segment-table-columns'
import Table from '@/components/table'
import { TextBody } from '@/components/text'
import useFormulaCriterionLookup from '@/features/formula/hooks/use-formula-criterion-lookup'
import { usePageantHierarchyQuery } from '@/features/pageants/hooks/use-pageant-hierarchy'
import { useSelectedPageantQuery } from '@/features/pageants/hooks/use-selected-pageant-query'
import FormulaRenderer from '@/features/formula/components/formula-renderer'

export const Route = createFileRoute('/admin/console/segments/')({
  component: AdminConsoleSegments,
})

function AdminConsoleSegments() {
  const { data: segments, isLoading } = useSegmentsQuery()
  const { data: selectedPageant } = useSelectedPageantQuery()
  const { data: pageantHierarchy } = usePageantHierarchyQuery(
    selectedPageant?.id,
  )
  const criterionLookup = useFormulaCriterionLookup(
    pageantHierarchy?.phases ?? [],
  )

  const criterionMap = useMemo(() => {
    const map = new Map<string, string>()
    if (!pageantHierarchy) return map

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
  const processedSegments = useMemo(() => {
    if (!segments) return []
    return segments.map((award) => {
      const rawFormula = award.formula
      return {
        ...award,
        /* Really patchy fix. But this prevents multiple query fetches and you cant call hooks in table-columns */
        formula: rawFormula ? (
          <FormulaRenderer
            formula={rawFormula}
            criterionLookup={criterionLookup}
          />
        ) : null,
      }
    })
  }, [segments, criterionMap, criterionLookup])

  return (
    <Console>
      <Console.Header>
        <Console.Header.Title>Segments</Console.Header.Title>
      </Console.Header>
      <Console.Content>
        {isLoading ? (
          <TextBody>Loading...</TextBody>
        ) : (
          <Table.Data columns={segmentTableColumns} data={processedSegments} />
        )}
      </Console.Content>
    </Console>
  )
}
