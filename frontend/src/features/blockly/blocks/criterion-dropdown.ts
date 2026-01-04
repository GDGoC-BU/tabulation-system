import * as Blockly from 'blockly'
import { useBlocklyStore } from '../store/use-blockly-store'

Blockly.defineBlocksWithJsonArray([
  {
    type: 'criterion_dropdown',
    message0: '%1',
    args0: [
      {
        type: 'field_dropdown',
        name: 'CRITERION',
        options: [['Dummy option, cant pass empty array or null.', 'X']],
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
    /* NOTE: This function runs whenever the dropdown is used! */
    const { criterionDropdownOptions } = useBlocklyStore.getState()

    let options = null
    if (criterionDropdownOptions === null) {
      options = [['Loading criterions', 'X']]
    } else if (criterionDropdownOptions.length === 0) {
      options = [['No criterions available', 'X']]
    } else {
      options = criterionDropdownOptions
    }

    return options
  })
})
