export default function deleteLastFormulaChunk(formula: string): string {
  if (!formula) return ''

  // Regex to match at the end of the string, one of:
  // - UUID (full)
  // - Number with optional decimal (e.g. 123, 0.45, .6)
  // - Single arithmetic operator or paren
  const pattern = new RegExp(
    [
      // UUID regex (match whole UUID at end)
      '\\s[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$',
      // Number regex (with optional decimal, including leading dot)
      '\\d*\\.?\\d+$',
      // Single operator or parenthesis
      '\\s[+\\-*/()]\\s$',
      // Just a trailing space (in case it’s left behind)
      '\\s$',
    ].join('|'),
  )

  const match = formula.match(pattern)
  if (!match) {
    // If nothing matches, just remove last char as fallback
    return formula.slice(0, -1)
  }

  // Remove matched chunk from the end
  return formula.slice(0, formula.length - match[0].length)
}
