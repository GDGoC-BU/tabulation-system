import { createFileRoute } from '@tanstack/react-router'
import { useQuery } from '@tanstack/react-query'
import { useAwardQuery } from '@/features/awards/hooks/use-award-query'
import { useAwardCalculation } from '@/features/awards/hooks/use-award-calculation-mutation'
import { TextBody, TextHeading } from '@/components/text'
import FormulaRenderer from '@/features/formula/deprecated-components/formula-renderer'
import useFormulaCriterionLookup from '@/features/formula/hooks/use-formula-criterion-lookup'
import { useSelectedPageant } from '@/features/pageants/hooks/use-selected-pageant'
import Table from '@/components/table'
import { awardLeadboardTableColumns } from '@/features/awards/components/award-leaderboard-columns'
import { groupLeaderboardByGender } from '@/lib/group-leaderboard-by-gender'
import pageantHierarchyQueryOptions from '@/features/pageants/query-options/pageant-hierarchy-query-options'

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
    useSelectedPageant()
  const { data: pageantHierarchy, isLoading: isPageantHierarchyLoading } =
    useQuery(
      pageantHierarchyQueryOptions(selectedPageant?.id, {
        enabled: !!selectedPageant,
      }),
    )
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
      <div className="border rounded-lg p-4 grow">
        <TextBody>Calculating {award?.name}...</TextBody>
      </div>
    )
  }

  const { groupA, groupB } = groupLeaderboardByGender(
    awardResult?.leaderboard ?? undefined,
  )

  return (
    <div className="p-4 grow">
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
                formula={awardResult?.formula.text ?? ''}
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
                formula={awardResult?.formula.text ?? ''}
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
                formula={awardResult?.formula.text ?? ''}
              />
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}
