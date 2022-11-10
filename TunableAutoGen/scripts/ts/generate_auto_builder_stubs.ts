import * as fsp from 'node:fs/promises'
import { splitMethodIntoSubMethods, getMethods, PATHS, addCodeIntoTemplate } from './util';

(async () => {
  const methods = await getMethods();
  const all_methods = methods.map(splitMethodIntoSubMethods).flat();

  const builder_stubs = all_methods.map(method => (
    `                                                                                                                          \n` +
    `    public TuningAutoBuilder ${method.name}(${method.params.map(param => `${param.type} ${param.name}`).join(', ')}) {    \n` +
    `        throw new RuntimeException("Stub!");                                                                              \n` +
    `    }                                                                                                                     \n`
  )).join('');

  const builder_file = await fsp.readFile(PATHS.DUMMY_BUILDER, 'utf-8');

  const new_builder_file_cleaned = addCodeIntoTemplate(builder_file, builder_stubs)
                                  .replace(/ *$/g, '');

  await fsp.writeFile(PATHS.DUMMY_BUILDER, new_builder_file_cleaned);
})();
