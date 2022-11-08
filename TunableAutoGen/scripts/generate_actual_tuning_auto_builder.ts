import * as fsp from 'node:fs/promises';
import { addCodeIntoTemplate, getMethods, PATHS, splitMethodIntoSubMethods } from './util';
import { Param } from './types';

(async () => {
  const builder_template = await fsp.readFile(PATHS.ACTUAL_BUILDER, 'utf-8');

  const methods = await getMethods();
  const all_methods = methods.map(splitMethodIntoSubMethods).flat();

  let actual_builder_methods = '';

  all_methods?.forEach(method => {
    const header_params = method.params.map(param => `${param.is_lambda ? 'String' : param.type} ${param.name}`).join(', ');

    const header = `public ActualTuningAutoBuilder ${method.name}(${header_params})`;

    const varTypeFor = (type: Param) => {
      switch (true) {
        case method.name.endsWith('T'):
          return 'TunableVar';
        case type.is_lambda:
          return 'Lambda';
        default:
          return 'Var';
      }
    };

    const var_objects_declaration = method.params.map((param, index) => (
      `Var param${index} = new ${varTypeFor(param)}("${param.name}", "${param.type}", ${param.name});`
    )).join('\n        ');

    const param_names = method.params.map((param, index) => `param${index}`).join(', ');

    actual_builder_methods +=
      `                                                                                             \n` +
      `    ${header} {                                                                              \n` +
      `        ${var_objects_declaration}                                                           \n` +
      `        addMethodCall("${method.name}", null, ${param_names});                               \n` +
      `        return this;                                                                         \n` +
      `    }                                                                                        \n`;
  });

  const new_builder_template_cleaned = addCodeIntoTemplate(builder_template, actual_builder_methods)
                                      .replace(/ *$/g, '');

  await fsp.writeFile(PATHS.ACTUAL_BUILDER, new_builder_template_cleaned);
})();
