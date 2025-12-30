import * as Blockly from 'blockly'

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
