import { Link, createFileRoute } from '@tanstack/react-router'
import { useMemo } from 'react'
import { useQuery } from '@tanstack/react-query'
import type {
  CandidateDetailed,
  CandidateHierarchy,
  CandidateSummary,
} from '@/features/candidates/schemas'
import type { ScoreDetailed } from '@/features/scores/schemas'
import type { ComponentClassNameProp } from '@/types'
import { useJudgesQuery } from '@/features/judges/hooks/use-judges-query'
import { useCandidatesQuery } from '@/features/candidates/hooks/use-candidates-query'
import { TextBody, TextHeading, TextSub } from '@/components/text'
import splitCandidates from '@/features/candidates/lib/split-candidates'
import capitalizeWords from '@/lib/capitalize-words'
import { cn } from '@/lib/utils'
import { Badge } from '@/components/ui/badge'
import { Button } from '@/components/ui/button'
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs'
import scoresQueryOptions from '@/features/scores/query-options/scores-query-options'
import Loading from '@/components/loading'
import segmentQueryOptions from '@/features/segments/query-options/segment-query-options'

function CandidateScoreCard({
  candidate,
  scores,
  className,
}: {
  candidate: CandidateSummary | CandidateDetailed | CandidateHierarchy
  scores: Array<ScoreDetailed>
} & ComponentClassNameProp) {
  const totalScore = useMemo(() => {
    return scores.reduce((a, b) => a + b.value, 0)
  }, [scores])

  const totalMaxScore = useMemo(() => {
    return scores.reduce((a, b) => a + b.criterion.maxScore, 0)
  }, [scores])

  return (
    <div
      className={cn('bg-background rounded-xl border flex flex-col', className)}
    >
      <div className="px-8 py-6 flex flex-row items-center gap-2 justify-between w-full border-b">
        <div className="flex flex-row items-center gap-2">
          <div className="bg-black w-fit px-4 aspect-square rounded-full grid place-items-center">
            <TextHeading className="text-background">
              {candidate.number}
            </TextHeading>
          </div>
          <div className="flex flex-col">
            <TextBody className="mt-2">
              {capitalizeWords(candidate.firstName)}{' '}
              {capitalizeWords(candidate.lastName)}
            </TextBody>
            <TextSub className="mt-0">BU College of Science</TextSub>
          </div>
        </div>
        <div className="size-fit relative">
          <div className="border size-fit px-2 py-1 rounded-full flex flex-row gap-1 items-center">
            <TextBody className="text-foreground">
              {totalScore} / {totalMaxScore}
            </TextBody>
          </div>
        </div>
      </div>
      <div className="px-8 py-6 grid place-items-center w-full">
        <div className="flex flex-col gap-2">
          {scores
            .sort((a, b) => {
              /* First sort by maxScore */
              if (a.criterion.maxScore !== b.criterion.maxScore) {
                return a.criterion.maxScore - b.criterion.maxScore
              }
              /* Then sort by name */
              return a.criterion.name.localeCompare(b.criterion.name)
            })
            .map((score) => {
              return (
                <div
                  key={score.id}
                  className="w-full grid grid-cols-[1fr_auto_auto] gap-2 items-center"
                >
                  <TextBody className="text-end whitespace-nowrap">
                    {score.criterion.name}
                  </TextBody>
                  <TextBody>:</TextBody>
                  <TextBody className="text-[20px] leading-[28px] font-[600] tracking-[0]">
                    {score.value} / {score.criterion.maxScore}
                  </TextBody>
                </div>
              )
            })}
        </div>
      </div>
    </div>
  )
}

export const Route = createFileRoute(
  '/admin/console/segments/$segmentId/scores',
)({
  component: RouteComponent,
})

function RouteComponent() {
  const { segmentId } = Route.useParams()
  const {
    data: judges,
    isLoading: isJudgeLoading,
    isError: isJudgeError,
  } = useJudgesQuery()
  const {
    data: candidates,
    isLoading: isCandidatesLoading,
    isError: isCandidatesError,
  } = useCandidatesQuery()
  const {
    data: segment,
    isLoading: isSegmentLoading,
    isError: isSegmentError,
  } = useQuery(segmentQueryOptions(segmentId))

  const {
    data: scores,
    isLoading: isScoresLoading,
    isError: isScoresError,
    refetch: refetchScores,
  } = useQuery(
    scoresQueryOptions(
      {
        segmentId: segment?.id,
      },
      {
        enabled: !!segment,
      },
    ),
  )

  const { femaleCandidates, maleCandidates } = useMemo(() => {
    if (!segment) {
      return {
        femaleCandidates: [],
        maleCandidates: [],
      }
    }

    return splitCandidates(candidates ?? [])
  }, [segment])

  function onRefetch() {
    refetchScores()
  }

  if (
    isJudgeLoading ||
    isCandidatesLoading ||
    isSegmentLoading ||
    isScoresLoading
  ) {
    return (
      <div className="w-full grow grid place-items-center">
        <Loading />
      </div>
    )
  }

  if (isJudgeError || isCandidatesError || isSegmentError || isScoresError) {
    return (
      <div className="w-full grow grid place-items-center">
        <TextBody>Something went wrong!</TextBody>
      </div>
    )
  }

  return (
    <div className="grow select-none px-4 pb-4 flex flex-col">
      <div className="py-8 relative w-full flex flex-row items-center justify-center">
        <div className="absolute left-0 flex flex-row gap-2">
          <Button className="" asChild>
            <Link to="/admin/console/segments">Back</Link>
          </Button>
          <Button onClick={onRefetch} variant="outline">
            Refetch Scores
          </Button>
        </div>

        <div className="flex flex-row justify-center">
          <div className="flex flex-col gap-2">
            <TextHeading>{segment?.name} Scores</TextHeading>
          </div>
        </div>
      </div>

      <Tabs defaultValue={judges ? judges[0].id : ''} className="w-full">
        <TabsList className="w-full">
          {judges?.map((judge) => {
            return (
              <TabsTrigger key={judge.id} value={judge.id}>
                Judge {judge.number}
              </TabsTrigger>
            )
          })}
        </TabsList>
        {judges?.map((judge) => {
          return (
            <TabsContent key={judge.id} value={judge.id}>
              <div className="border rounded-lg flex flex-col">
                <div className="px-4 py-8 flex flex-col items-center gap-2 border-b">
                  <TextHeading>Judge {judge.number}</TextHeading>
                  <TextBody>
                    {capitalizeWords(judge.honorific) + '.'}{' '}
                    {capitalizeWords(judge.firstName)}{' '}
                    {capitalizeWords(judge.lastName)}
                  </TextBody>
                </div>
                <div className="px-4 pt-4 pb-4 grid grid-cols-2 gap-4">
                  <div className="flex flex-col gap-8 bg-muted border rounded-xl px-4 pb-4 pt-8">
                    <div className="flex flex-row justify-center">
                      <Badge className="bg-gender-female-primary">
                        <TextBody className="text-background">
                          Female Candidates
                        </TextBody>
                      </Badge>
                    </div>
                    <div className="grid grid-cols-2 gap-4">
                      {femaleCandidates.map((candidate) => {
                        const candidateScores = scores?.filter(
                          (score) =>
                            score.candidateId === candidate.id &&
                            score.judgeId === judge.id,
                        )

                        return (
                          <CandidateScoreCard
                            key={candidate.id}
                            className="shadow-lg shadow-gender-female-secondary"
                            candidate={candidate}
                            scores={candidateScores ?? []}
                          />
                        )
                      })}
                    </div>
                  </div>
                  <div className="flex flex-col gap-8 bg-muted border rounded-xl px-4 pb-4 pt-8">
                    <div className="flex flex-row justify-center">
                      <Badge className="bg-gender-male-primary">
                        <TextBody className="text-background">
                          Male Candidates
                        </TextBody>
                      </Badge>
                    </div>
                    <div className="grid grid-cols-2 gap-4">
                      {maleCandidates.map((candidate) => {
                        const candidateScores = scores?.filter(
                          (score) =>
                            score.candidateId === candidate.id &&
                            score.judgeId === judge.id,
                        )

                        return (
                          <CandidateScoreCard
                            key={candidate.id}
                            className="shadow-lg shadow-gender-male-secondary"
                            candidate={candidate}
                            scores={candidateScores ?? []}
                          />
                        )
                      })}
                    </div>
                  </div>
                </div>
              </div>
            </TabsContent>
          )
        })}
        <TabsContent value="password">Change your password here.</TabsContent>
      </Tabs>
    </div>
  )
}
