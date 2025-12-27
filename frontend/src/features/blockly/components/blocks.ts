import * as Blockly from 'blockly'

Blockly.defineBlocksWithJsonArray([
  {
    type: 'number_literal',
    message0: '%1',
    args0: [
      {
        type: 'field_number',
        name: 'VALUE',
        value: 0,
      },
    ],
    output: 'Number',
    colour: 230,
  },
  {
    type: 'add_numbers',
    message0: '%1 + %2',
    args0: [
      {
        type: 'input_value',
        name: 'A',
        check: 'Number',
      },
      {
        type: 'input_value',
        name: 'B',
        check: 'Number',
      },
    ],
    output: 'Number',
    colour: 230,
  },
])
