const fs = require('fs').promises;

const ACTIONS_FILE_PATH = '../test_autobuilder.json';

(async () => {
  const actions = JSON.parse(await fs.readFile(ACTIONS_FILE_PATH));
  actions.sort((a, b) => a.index - b.index);

  console.log(actions);

  let output = "";
  let vars = "";

  output +=
    `
    import com.acmerobotics.dashboard.config.Config;
    import org.firstinspires.ftc.teamcodekt.components.scheduler.auto.AutoProvider;

    @Config
    public class AutoProvideImpl implements AutoProvider {
      @Override
      public void scheduleAuto() {
    `

  for i
})();