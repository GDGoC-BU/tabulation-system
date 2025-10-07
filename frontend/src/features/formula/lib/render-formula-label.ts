export default function renderFormulaLabel(
  rawFormula: string,
  criterionMap: Map<string, string>,
): string {
  // This regex matches any UUID in the formula
  const uuidRegex =
    /[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}/g

  // Replace each UUID in the formula with its label
  const replaced = rawFormula.replace(uuidRegex, (uuid) => {
    const label = criterionMap.get(uuid)
    return label ? label : uuid
  })

  // Add spaces around arithmetic operators for readability
  const spaced = replaced.replace(/([+\-*/()])/g, ' $1 ')

  // Collapse multiple spaces and trim
  return spaced.replace(/\s+/g, ' ').trim()
}
