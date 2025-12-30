import * as Blockly from 'blockly'

/* -------------------------------
   Number literal block
-------------------------------- */

Blockly.defineBlocksWithJsonArray([
  {
    type: 'number-literal',
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
