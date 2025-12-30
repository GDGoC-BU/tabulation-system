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
          value: 0,
          check: 'Number',
        },
      ],
      colour: 200,
    })
    this.setDeletable(false)
    this.setMovable(false)
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
