import { Order, javascriptGenerator } from 'blockly/javascript'

javascriptGenerator.forBlock['formula_root'] = function (block, generation) {
  return `${generation.valueToCode(block, 'FORMULA_RESULT', Order.NONE)}`
}
