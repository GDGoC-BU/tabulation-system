import { Order, javascriptGenerator } from 'blockly/javascript'

javascriptGenerator.forBlock['binary_operation'] = function (block, generator) {
  const OPERATORS = {
    ADD: { label: '+', order: Order.ADDITION },
    MINUS: { label: '-', order: Order.SUBTRACTION },
    MULTIPLY: { label: '*', order: Order.MULTIPLICATION },
    DIVIDE: { label: '/', order: Order.DIVISION },
    EXPONENT: { label: '^', order: Order.EXPONENTIATION },
  }

  const operator =
    OPERATORS[block.getFieldValue('OPERATOR') as keyof typeof OPERATORS]

  const left = generator.valueToCode(block, 'LEFT_VALUE', operator.order)
  const right = generator.valueToCode(block, 'RIGHT_VALUE', operator.order)

  return [`${left} ${operator.label} ${right}`, operator.order]
}
