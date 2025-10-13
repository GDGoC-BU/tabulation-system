import { useQueryClient } from '@tanstack/react-query'
import type { AwardDetailed } from '@/features/awards/schemas'
import { TextBody, TextHeading } from '@/components/text'
import { Button } from '@/components/ui/button'
import { useSelectedPageantQuery } from '@/features/pageants/hooks/use-selected-pageant-query'
import useStatusChangeMutate from '@/features/state-machine/hooks/use-status-change-mutate'
import { useAwardsQuery } from '@/features/awards/hooks/use-awards-query'
import { usePageantHierarchyQuery } from '@/features/pageants/hooks/use-pageant-hierarchy'
import useFormulaCriterionLookup from '@/features/formula/hooks/use-formula-criterion-lookup'
import FormulaRenderer from '@/features/formula/components/formula-renderer'
import { useAwardCalculations } from '@/features/awards/hooks/use-award-calculation-mutation'
import { cn } from '@/lib/utils'
import { candidateGender } from '@/features/candidates/schemas'

function groupLeaderboardByGender(award: AwardDetailed | undefined) {
  if (!award) return

  const groupA = award.leaderboard.filter(
    (entry) => entry.candidate.gender === candidateGender.enum.FEMALE,
  )

  const groupB = award.leaderboard.filter(
    (entry) => entry.candidate.gender === candidateGender.enum.MALE,
  )

  return { groupA, groupB }
}

function AwardLeaderboard({
  leaderboard,
  limit,
}: {
  leaderboard: AwardDetailed['leaderboard']
  limit: number
}) {
  return leaderboard.map((row, index) => {
    return (
      <div
        className={cn(
          'border grid grid-cols-[50px_150px_1fr_max-content] items-center rounded-md divide-x',
          index < limit ? 'border-emerald-500' : 'border-red-600',
        )}
      >
        <div
          className={cn(
            ' text-center my-2',
            index < limit ? 'border-emerald-500' : 'border-red-600',
          )}
        >
          <TextBody>{index + 1}</TextBody>
        </div>
        <div
          className={cn(
            'px-4',
            index < limit ? 'border-emerald-500' : 'border-red-600',
          )}
        >
          <TextBody className="">Candidate {row.candidate.number}</TextBody>
        </div>
        <div
          className={cn(
            'px-4',
            index < limit ? 'border-emerald-500' : 'border-red-600',
          )}
        >
          <TextBody>
            {row.candidate.firstName} {row.candidate.lastName}
          </TextBody>
        </div>
        <div className="grid grid-cols-2 gap-2 px-4">
          <TextBody className="font-bold">Score:</TextBody>
          <TextBody className="font-bold">{row.score.toFixed(3)}</TextBody>
        </div>
      </div>
    )
  })
}

export default function PageantFinalizingDashboard() {
  const queryClient = useQueryClient()

  const { mutateAsync } = useStatusChangeMutate()
  const { data: selectedPageant, isLoading } = useSelectedPageantQuery()
  const { data: awards } = useAwardsQuery()
  const awardResults = useAwardCalculations(awards)

  const { data: pageantHierarchy } = usePageantHierarchyQuery(
    selectedPageant?.id,
  )
  const criterionLookup = useFormulaCriterionLookup(
    pageantHierarchy?.phases ?? [],
  )

  if (isLoading || !selectedPageant) {
    return <TextBody>Loading Finalizing Dashboard...</TextBody>
  }

  async function onClick() {
    await mutateAsync(`/pageants/${selectedPageant?.id}/close`)
    queryClient.invalidateQueries({ queryKey: ['pageants'] })
    queryClient.invalidateQueries({
      queryKey: ['pageants', selectedPageant?.id],
    })
  }

  return (
    <div className="w-full flex flex-col gap-4">
      <div className="">
        <Button onClick={onClick}>Close Pageant</Button>
      </div>
      <div className="w-full flex flex-col gap-24">
        {awardResults.map(({ data: award, isLoading, isError }) => {
          if (isError) {
            return (
              <div className="border rounded-lg p-4">
                <TextBody>Error calculating {award?.name}</TextBody>
              </div>
            )
          }

          if (isLoading) {
            return (
              <div className="border rounded-lg p-4">
                <TextBody>Calculating {award?.name}...</TextBody>
              </div>
            )
          }

          const { groupA, groupB } = groupLeaderboardByGender(award) ?? {
            groupA: [],
            groupB: [],
          }

          return (
            <div className="border rounded-lg p-4">
              <div className="mb-2">
                <TextHeading>{award?.name}</TextHeading>
              </div>
              <div className="flex flex-col mb-8 pb-8 gap-2 border-b">
                <div className="">
                  <TextBody>Candidate Limit: {award?.candidateLimit}</TextBody>
                </div>
                <div className="">
                  <FormulaRenderer
                    formula={award?.formula ?? ''}
                    criterionLookup={criterionLookup}
                  />
                </div>
              </div>
              <div className="w-full grid grid-cols-2 gap-4">
                <div className="flex flex-col gap-2">
                  <div className="w-full text-center mb-8">
                    <TextBody className="font-bold">Female</TextBody>
                  </div>
                  <AwardLeaderboard
                    leaderboard={groupA}
                    limit={award?.candidateLimit ?? 0}
                  />
                </div>
                <div className="flex flex-col gap-2">
                  <div className="w-full text-center mb-8">
                    <TextBody className="font-bold">Male</TextBody>
                  </div>
                  <AwardLeaderboard
                    leaderboard={groupB}
                    limit={award?.candidateLimit ?? 0}
                  />
                </div>
              </div>
            </div>
          )
        })}
      </div>
    </div>
  )
}
