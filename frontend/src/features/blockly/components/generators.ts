import { javascriptGenerator } from 'blockly/javascript'

javascriptGenerator.forBlock['number_literal'] = (block) => {
  const value = block.getFieldValue('VALUE')
  return `${value}`
}
