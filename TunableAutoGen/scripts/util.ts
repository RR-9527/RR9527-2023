import { MethodHeader } from './types';
import * as fsp from 'node:fs/promises';

export const PATHS = {
  METHODS_JSON: 'TunableAutoGen/auto_builder_methods.json',
  ACTUAL_BUILDER: 'TunableAutoGen/src/main/java/org/tunableautogen/builder/ActualTuningAutoBuilder.java',
  DUMMY_BUILDER: 'TunableAutoGen/src/main/java/org/tunableautogen/builder/TuningAutoBuilder.java'
};

export async function getMethods(): Promise<MethodHeader[]> {
  const methods_json = await fsp.readFile(PATHS.METHODS_JSON, 'utf-8');
  return JSON.parse(methods_json) as MethodHeader[];
}

export function splitMethodIntoSubMethods(method: MethodHeader): MethodHeader[] {
  return _splitMethodIntoSubMethods({
    name: method.name,
    params: [...method.params, { name: 'tag', type: 'String', optional: true }]
  }).filter(unique);
}

export function addCodeIntoTemplate(template: string, code: string): string {
  const template_start_index = template.indexOf('// -- START --') + '// -- START --'.length;
  const template_end_index = template.indexOf('    // -- END --');

  return (
    template.substring(0, template_start_index) +
    '\n' +
    code +
    '\n' +
    template.substring(template_end_index)
  );
}

function _splitMethodIntoSubMethods(method: MethodHeader): MethodHeader[] {
  const ret: MethodHeader[] = [];

  method.params.filter(param => param.optional).forEach(param => {
    ret.push(..._splitMethodIntoSubMethods({
      name: method.name,
      params: method.params.filter(p => p.name != param.name)
    }));
  });

  ret.push(method);
  ret.push({ ...method, name: method.name + 'T' });

  return ret;
}

function unique<T>(value: T, index: number, self: T[]): boolean {
  return index === self.findIndex(obj => {
    return JSON.stringify(obj) === JSON.stringify(value);
  });
}
