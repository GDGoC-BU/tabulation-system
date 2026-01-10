import * as Blockly from 'blockly'

Blockly.defineBlocksWithJsonArray([
  {
    type: 'binary_operation',
    message0: '%1 %2 %3',
    args0: [
      {
        type: 'input_value',
        name: 'LEFT_VALUE',
        check: ['Number', 'String'],
      },
      {
        type: 'field_dropdown',
        name: 'OPERATOR',
        options: [
          ['+', 'ADD'],
          ['-', 'SUBTRACT'],
          ['×', 'MULTIPLY'],
          ['÷', 'DIVIDE'],
        ],
      },
      {
        type: 'input_value',
        name: 'RIGHT_VALUE',
        check: ['Number', 'String'],
      },
    ],
    output: 'Number',
    colour: 230,
    inputsInline: true,
  },
])
