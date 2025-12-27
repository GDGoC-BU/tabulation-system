import { javascriptGenerator } from 'blockly/javascript'

const JS = javascriptGenerator as typeof javascriptGenerator & {
  ORDER_ATOMIC: number
  ORDER_ADDITION: number
}

javascriptGenerator.forBlock['number_literal'] = (block) => {
  const value = block.getFieldValue('VALUE')
  return [value, JS.ORDER_ATOMIC]
}

javascriptGenerator.forBlock['add_numbers'] = (block) => {
  const a =
    javascriptGenerator.valueToCode(block, 'A', JS.ORDER_ADDITION) || '0'

  const b =
    javascriptGenerator.valueToCode(block, 'B', JS.ORDER_ADDITION) || '0'

  return [`${a} + ${b}`, JS.ORDER_ADDITION]
}
