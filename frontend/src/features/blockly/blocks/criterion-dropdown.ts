import * as Blockly from 'blockly'
import { useCriterionDropdownStore } from './../store/use-criterion-dropdown'

Blockly.defineBlocksWithJsonArray([
  {
    type: 'criterion_dropdown',
    message0: '%1',
    args0: [
      {
        type: 'field_dropdown',
        name: 'CRITERION',
        options: [['Loading...', 'X']],
      },
    ],
    output: 'String',
    extensions: ['criterion_dropdown_extension'],
    colour: 10,
  },
])

Blockly.Extensions.register('criterion_dropdown_extension', function () {
  // @ts-ignore. Code below is pulled from blockly documention. Find a way to make it typesafe
  this.getField('CRITERION').setOptions(function () {
    /*
    NOTE: This function runs whenever the dropdown is used!
    */
    const { criterionLookup } = useCriterionDropdownStore.getState()
    if (!criterionLookup) return [['Loading...', 'X']]

    const sortedCriteria = Object.values(criterionLookup).sort((a, b) => {
      if (a.phase.sequence !== b.phase.sequence) {
        return a.phase.sequence - b.phase.sequence
      }
      return a.segment.sequence - b.segment.sequence
    })

    const options: Array<[string, string] | 'separator'> = []
    let currentSequence: null | number = null

    sortedCriteria.forEach((criterion) => {
      /* Add separator after each segment */
      if (!currentSequence) {
        currentSequence = criterion.segment.sequence
      }
      if (currentSequence != criterion.segment.sequence) {
        options.push('separator')
        currentSequence = criterion.segment.sequence
      }

      options.push([
        `${criterion.phase.name} / ${criterion.segment.name} / ${criterion.criterion.name}`,
        `${criterion.criterion.id}`,
      ])
    })
    return options
  })
})

/*
Blockly.Extensions.register('criterion_dropdown_extension', function () {
  this.getField('CRITERION').setOptions(function () {
    const options: Array<[string, string]> = []
    dummyCriteria.forEach((criterion) => {
      options.push([criterion.name, criterion.id])
    })
    return options
  })
})
*/
