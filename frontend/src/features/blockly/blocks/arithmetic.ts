import * as Blockly from 'blockly'

Blockly.defineBlocksWithJsonArray([
  {
    type: 'arithmetic',
    message0: '%1 %2 %3',
    args0: [
      {
        type: 'input_value',
        name: 'LEFT_VALUE',
        check: 'Number',
      },
      {
        type: 'field_dropdown',
        name: 'OPERATOR',
        options: [
          ['+', 'add'],
          ['-', 'minus'],
          ['×', 'multiply'],
          ['÷', 'divide'],
          ['^', 'exponent'],
        ],
      },
      {
        type: 'input_value',
        name: 'RIGHT_VALUE',
        check: 'Number',
      },
    ],
    output: 'Number',
    colour: 230,
    inputsInline: true,
  },
])
