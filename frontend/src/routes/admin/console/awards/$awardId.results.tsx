import { createFileRoute } from '@tanstack/react-router'
import { useAwardQuery } from '@/features/awards/hooks/use-award-query'
import { useAwardCalculation } from '@/features/awards/hooks/use-award-calculation-mutation'
import { TextBody, TextHeading } from '@/components/text'
import FormulaRenderer from '@/features/formula/components/formula-renderer'
import { usePageantHierarchyQuery } from '@/features/pageants/hooks/use-pageant-hierarchy'
import useFormulaCriterionLookup from '@/features/formula/hooks/use-formula-criterion-lookup'
import { useSelectedPageantQuery } from '@/features/pageants/hooks/use-selected-pageant-query'
import Table from '@/components/table'
import { awardLeadboardTableColumns } from '@/features/awards/components/award-leaderboard-columns'
import { groupLeaderboardByGender } from '@/lib/group-leaderboard-by-gender'

export const Route = createFileRoute('/admin/console/awards/$awardId/results')({
  component: RouteComponent,
})

function RouteComponent() {
  const { awardId } = Route.useParams()
  const { data: award, isLoading: isAwardLoading } = useAwardQuery(awardId)
  const {
    data: awardResult,
    isLoading: isResultLoading,
    isError,
  } = useAwardCalculation(award?.id)

  const { data: selectedPageant, isLoading: isPageantStateLoading } =
    useSelectedPageantQuery()
  const { data: pageantHierarchy, isLoading: isPageantHierarchyLoading } =
    usePageantHierarchyQuery(selectedPageant?.id)
  const criterionLookup = useFormulaCriterionLookup(
    pageantHierarchy?.phases ?? [],
  )

  if (isError) {
    return (
      <div className="border rounded-lg p-4">
        <TextBody>Error calculating {award?.name}</TextBody>
      </div>
    )
  }

  if (isPageantStateLoading || isPageantHierarchyLoading) {
    return (
      <div className="border rounded-lg p-4">
        <TextBody>Loading pageant...</TextBody>
      </div>
    )
  }

  if (isResultLoading || isAwardLoading) {
    return (
      <div className="border rounded-lg p-4">
        <TextBody>Calculating {award?.name}...</TextBody>
      </div>
    )
  }

  const { groupA, groupB } = groupLeaderboardByGender(
    awardResult?.leaderboard ?? undefined,
  )

  return (
    <div className="p-4">
      <div className="w-full flex flex-col gap-24">
        <div className="border rounded-lg p-4">
          <div className="mb-2">
            <TextHeading>{awardResult?.name}</TextHeading>
          </div>
          <div className="flex flex-col gap-2 border-b pb-8">
            <div className="">
              <TextBody>
                Candidate Limit: {awardResult?.candidateLimit}
              </TextBody>
            </div>
            <div className="flex flex-row gap-2">
              <TextBody>Formula: </TextBody>
              <FormulaRenderer
                formula={awardResult?.formula ?? ''}
                criterionLookup={criterionLookup}
              />
            </div>
          </div>
          <div className="w-full flex flex-col">
            <div className="flex flex-col gap-2 mt-8">
              <div className="w-full text-center mb-8">
                <TextHeading className="font-bold">Female</TextHeading>
              </div>
              <Table.Leaderboard
                columns={awardLeadboardTableColumns}
                data={groupA}
                limit={awardResult?.candidateLimit ?? 0}
                formula={awardResult?.formula ?? ''}
              />
            </div>
            <div className="flex flex-col gap-2 mt-8">
              <div className="w-full text-center mb-8">
                <TextHeading className="font-bold">Male</TextHeading>
              </div>
              <Table.Leaderboard
                columns={awardLeadboardTableColumns}
                data={groupB}
                limit={awardResult?.candidateLimit ?? 0}
                formula={awardResult?.formula ?? ''}
              />
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}
