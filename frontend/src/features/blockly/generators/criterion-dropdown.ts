import { Order, javascriptGenerator } from 'blockly/javascript'

javascriptGenerator.forBlock['criterion_dropdown'] = function (
  block,
  generation,
) {
  const value = block.getFieldValue('CRITERION')
  return [String(value), Order.ATOMIC]
}
