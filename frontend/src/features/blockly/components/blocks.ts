import * as Blockly from 'blockly'

/* -------------------------------
   Root Formula Block
-------------------------------- */
Blockly.Blocks['formula_root'] = {
  init: function () {
    this.jsonInit({
      message0: 'Formula %1',
      args0: [
        {
          type: 'input_value',
          name: 'FORMULA_RESULT',
          check: 'Number',
        },
      ],
      colour: 200,
    })
    this.setDeletable(false)
    this.setEditable(false)
  },
}

/* -------------------------------
   Number literal block
-------------------------------- */

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
])

/* -------------------------------
   Arithmetic
-------------------------------- */
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
