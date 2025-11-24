import tokenizeFormula from '../lib/tokenize-formula'
import type { CriteriaBreakdown } from '@/schemas'
import { Badge } from '@/components/ui/badge'
import { TextBody, TextSub } from '@/components/text'
import {
  Tooltip,
  TooltipContent,
  TooltipTrigger,
} from '@/components/ui/tooltip'

export default function BreakdownRenderer({
  breakdown,
  formula,
}: {
  breakdown: Array<CriteriaBreakdown> | null
  formula: string
}) {
  if (!breakdown) {
    return <TextBody>No formula passed</TextBody>
  }

  const tokens = tokenizeFormula(formula)
  const criterionMap = new Map(breakdown.map((br) => [br.criterion.id, br]))

  return (
    <div className="flex flex-row gap-2 items-center flex-wrap">
      {tokens.map((token, index) => {
        if (token.type === 'uuid') {
          const br = criterionMap.get(token.value)

          if (!br) {
            return
          }

          const phaseName = br.phase.name
          const segmentName = br.segment.name
          const criterionName = br.criterion.name
          const maxScore = br.criterion.maxScore
          const averageScore = br.averageScore
          const scores = br.scores
          const numberOfJudge = scores.length

          return (
            <Tooltip>
              <TooltipTrigger asChild>
                <Badge key={index} variant="outline" className="">
                  {averageScore}
                </Badge>
              </TooltipTrigger>
              <TooltipContent className="flex flex-col items-center gap-4 p-4">
                <div className="flex flex-col items-center gap-1">
                  <div className="flex flex-row gap-1 [&>*]:text-background">
                    <TextSub>{phaseName}</TextSub>
                    <TextSub>/</TextSub>
                    <TextSub>{segmentName}</TextSub>
                    <TextSub>/</TextSub>
                    <TextSub>{criterionName}</TextSub>
                  </div>
                  <div>
                    <TextSub className="text-background">
                      Max Score: {maxScore}
                    </TextSub>
                  </div>
                </div>
                <div className="flex flex-col items-center gap-1">
                  <div className="flex flex-row gap-2 items-end">
                    {scores
                      .sort((a, b) => a.judge.number - b.judge.number)
                      .map((score, index) => {
                        const judgeNumber = score.judge.number
                        const judgeScore = score.value

                        return (
                          <>
                            <div className="flex flex-col items-center">
                              <div className="">
                                <TextSub className="text-background">
                                  Judge {judgeNumber}
                                </TextSub>
                              </div>
                              <div className="">
                                <TextSub className="text-background">
                                  {judgeScore}
                                </TextSub>
                              </div>
                            </div>
                            {index < numberOfJudge - 1 && (
                              <div className="">
                                <TextSub className="text-background">+</TextSub>
                              </div>
                            )}
                          </>
                        )
                      })}
                  </div>
                  <div className="h-[1px] bg-border w-full" />
                  <div className="">
                    <TextSub className="text-background">
                      {numberOfJudge}
                    </TextSub>
                  </div>
                </div>
              </TooltipContent>
            </Tooltip>
          )
        }

        return <TextBody>{token.value}</TextBody>
      })}
    </div>
  )
}
