const uuidRegex =
  /[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}/
const numberRegex = /(?:\d+\.\d*|\.\d+|\d+)/

const tokenRegex = new RegExp(
  `${uuidRegex.source}|${numberRegex.source}|[+\\-*/()]`,
  'g',
)

/**
 * Tokenizes a formula into UUIDs, numbers, operators, and parentheses.
 * Ignores spaces.
 *
 * @param {string} formula
 * @returns {Array<{ type: string, value: string }>}
 */
export default function tokenizeFormula(formula: string) {
  const tokens = []
  const matches = formula.matchAll(tokenRegex)

  for (const match of matches) {
    const value = match[0]

    if (uuidRegex.test(value)) {
      tokens.push({ type: 'uuid', value })
    } else if (/^[+\-*/]$/.test(value)) {
      tokens.push({ type: 'operator', value })
    } else if (/^[()]$/.test(value)) {
      tokens.push({ type: 'parenthesis', value })
    } else if (numberRegex.test(value)) {
      tokens.push({ type: 'number', value })
    }
  }

  return tokens
}
