import type { Phases } from '@/features/phases/schemas'
import type { Segments } from '@/features/segments/schemas'
import { phaseSegmentStatusValue } from '@/schemas'

type NextAction = {
  endpoint: string
  label: string
} | null

export default function determineNextPhaseSegmentStatusAction(
  phases: Phases,
  segments: Segments,
): NextAction {
  /* Note: At this point Phases and Segments should be ordered by sequence! */

  const PENDING = phaseSegmentStatusValue.enum.PENDING
  const ONGOING = phaseSegmentStatusValue.enum.ONGOING
  const CLOSED = phaseSegmentStatusValue.enum.CLOSED

  /* Get the first Phase that is not CLOSED */
  const phase = phases.find((p) => p.status !== CLOSED)
  /* If all are CLOSED, we are all done! */
  if (!phase) {
    return null
  }

  /* If Phase is still PENDING, start it */
  if (phase.status === PENDING) {
    return {
      endpoint: `/phases/${phase.id}/start`,
      label: `Start ${phase.name}`,
    }
  }

  /* If Phase is ONGOING, handle its Segments */
  if (phase.status === ONGOING) {
    /* Get the Segments for the Phase */
    const segmentsForPhase = segments
      .filter((segment) => segment.phase.id === phase.id)
      .sort((a, b) => a.sequence - b.sequence)

    /* Find the first non-CLOSED segment */
    const segment = segmentsForPhase.find(
      (segment) => segment.status !== CLOSED,
    )

    /* If all segments are CLOSED, close the Phase */
    if (!segment) {
      return {
        endpoint: `/phases/${phase.id}/close`,
        label: `Close ${phase.name}`,
      }
    }

    /* If Segment is PENDING, start it */
    if (segment.status === PENDING) {
      return {
        endpoint: `/segments/${segment.id}/start`,
        label: `Start ${segment.name}`,
      }
    }

    /* If Segment is ONGOING, close it */
    if (segment.status === ONGOING) {
      return {
        endpoint: `/segments/${segment.id}/close`,
        label: `Close ${segment.name}`,
      }
    }
  }

  return null
}
