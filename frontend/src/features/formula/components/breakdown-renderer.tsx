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

          return (
            <Tooltip>
              <TooltipTrigger asChild>
                <Badge key={index} variant="outline" className="">
                  {br?.averageScore}
                </Badge>
              </TooltipTrigger>
              <TooltipContent className="flex flex-col items-center gap-4 p-4">
                <div className="flex flex-col items-center gap-1">
                  <div className="flex flex-row gap-1 [&>*]:text-background">
                    <TextSub>{br?.phase.name}</TextSub>
                    <TextSub>/</TextSub>
                    <TextSub>{br?.segment.name}</TextSub>
                    <TextSub>/</TextSub>
                    <TextSub>{br?.criterion.name}</TextSub>
                  </div>
                  <div>
                    <TextSub className="text-background">
                      Max Score: {br?.criterion.maxScore}
                    </TextSub>
                  </div>
                </div>
                <div className="flex flex-col items-center gap-1">
                  <div className="flex flex-row gap-2">
                    {br?.scores.map((score, index) => {
                      return (
                        <>
                          <div className="flex flex-col items-center">
                            <div className="">
                              <TextSub className="text-background">
                                {score.judge.lastName}
                              </TextSub>
                            </div>
                            <div className="">
                              <TextSub className="text-background">
                                {score.value}
                              </TextSub>
                            </div>
                          </div>
                          {index < br.scores.length - 1 && (
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
                      {br?.scores.length}
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
