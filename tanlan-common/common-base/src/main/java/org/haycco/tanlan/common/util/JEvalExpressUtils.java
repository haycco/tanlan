package org.haycco.tanlan.common.util;

import java.util.HashMap;
import java.util.Map;
import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * 表达式计算
 *
 * @author zhangfang
 */
public class JEvalExpressUtils {

    public static Object calculate(String jExpress, Map<String, Object> params) {
        try {
            ScriptEngine scriptEngine = new ScriptEngineManager().getEngineByName("js");
            Compilable compilable = (Compilable) scriptEngine;

            CompiledScript compiledScript = compilable.compile(jExpress);
            Bindings bindings = scriptEngine.createBindings();

            params.keySet().forEach(key -> bindings.put(key, params.get(key)));

            return compiledScript.eval(bindings);
        } catch (ScriptException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<>(16);
        map.put("x", 10);
        map.put("y", 22);
        System.out.println(calculate("x>=10 && y<=20", map));
    }

}
