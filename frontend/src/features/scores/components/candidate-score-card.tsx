import { useEffect, useState } from 'react'
import ScoreEditForm from './score-edit-form'
import type { ScoreDetailed } from '@/features/scores/schemas'
import type { CandidateHierarchy } from '@/features/candidates/schemas'
import { candidateGender } from '@/features/candidates/schemas'
import Scoring from '@/components/scoring'

export default function CandidateScoreCard({
  candidate,
  scores = [],
}: {
  candidate: CandidateHierarchy
  scores?: Array<ScoreDetailed>
}) {
  const [scoreValues, setScoreValues] = useState(() =>
    Object.fromEntries(scores.map((score) => [score.id, score.value])),
  )

  useEffect(() => {
    if (scores.length !== 0) {
      setScoreValues(
        Object.fromEntries(scores.map((score) => [score.id, score.value])),
      )
    }
  }, [scores])

  /* Prop drill and handle total score count client side to minimize expensive score fetching and keep it simple. */
  const handleScoreChange = (id: string, value: number) => {
    setScoreValues((prev) => ({
      ...prev,
      [id]: value,
    }))
  }

  const totalScore = Object.values(scoreValues).reduce(
    (sum, val) => sum + (isNaN(val) ? 0 : val),
    0,
  )

  /* Determine the badge for each gender */
  let GenderBadge = (
    <Scoring.Card.Header.Badge className="bg-purple-400 font-semibold">
      Mx.
    </Scoring.Card.Header.Badge>
  )
  if (candidate.gender === candidateGender.enum.FEMALE) {
    GenderBadge = (
      <Scoring.Card.Header.Badge className="bg-pink-400 font-semibold">
        Ms.
      </Scoring.Card.Header.Badge>
    )
  } else if (candidate.gender === candidateGender.enum.MALE) {
    GenderBadge = (
      <Scoring.Card.Header.Badge className="bg-blue-400 font-semibold">
        Mr.
      </Scoring.Card.Header.Badge>
    )
  }

  return (
    <Scoring.Card>
      <Scoring.Card.Header>
        <Scoring.Card.Header.Title>
          Candidate {candidate.number}
        </Scoring.Card.Header.Title>
        <Scoring.Card.Header.BadgeGroup>
          {GenderBadge}
          <Scoring.Card.Header.Badge>
            Total Score: {totalScore}
          </Scoring.Card.Header.Badge>
        </Scoring.Card.Header.BadgeGroup>
      </Scoring.Card.Header>
      <Scoring.Card.Content>
        <div className="flex flex-col gap-8">
          {scores.map((score) => {
            return (
              <ScoreEditForm
                key={score.id}
                score={score}
                onChangeScore={handleScoreChange}
              />
            )
          })}
        </div>
      </Scoring.Card.Content>
    </Scoring.Card>
  )
}
