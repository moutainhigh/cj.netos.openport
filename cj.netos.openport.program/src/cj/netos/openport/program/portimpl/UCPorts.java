package cj.netos.openport.program.portimpl;

import cj.netos.openport.program.portface.IUCPorts;
import cj.netos.openport.program.args.TestArg;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceRef;
import cj.studio.ecm.net.CircuitException;
import cj.studio.openport.ResponseClient;
import cj.studio.openport.client.IRequestAdapter;
import cj.ultimate.gson2.com.google.gson.Gson;
import cj.ultimate.gson2.com.google.gson.reflect.TypeToken;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CjService(name = "/uc.ports")
public class UCPorts implements IUCPorts {
    @CjServiceRef(refByName = "$openports.cj.studio.openport.client.IRequestAdapter")
    IRequestAdapter requestAdapter;
    @Override
    public String authenticate(String authName, String tenant, String principals, String password, long ttlMillis)
            throws CircuitException {
        String retvalue=requestAdapter.request("get","http/1.1", new HashMap<String,String>() {
            {
                put("Rest-StubFace", "cj.studio.backend.uc.stub.IAuthenticationStub");
                put("Rest-Command", "authenticate");
                put("cjtoken", "xxx");
            }
        }, new HashMap<String,String>() {
            {
                put("authName", authName);
                put("tenant",tenant);
                put("principals", principals);
                put("password", password);
                put("ttlMillis", ttlMillis+"");
            }
        }, null);

        Map<String,String> response=new Gson().fromJson(retvalue,new TypeToken<HashMap<String,String>>(){}.getType());
        if(!"200".equals(response.get("status"))){
            throw new CircuitException(response.get("status"),"uc响应错误："+response.get("message"));
        }
        return response.get("result");
    }

    @Override
    public List<TestArg> test2(TestArg arg, BigDecimal v) {
//        List<TestArg> list=new ArrayList<>();
//        list.add(arg);
//        return list;
        List<TestArg> list= Arrays.asList(arg);
        return list;
    }

    @Override
    public Map<Integer, TestArg> test(List<TestArg> list, List<TestArg> set, Map<Integer, TestArg> map)
            throws CircuitException {
        System.out.println(String.format("--------list---%s", list));
        System.out.println(String.format("--------set---%s", set));
        System.out.println(String.format("--------map---%s", map));

//		try {
//			throw new InvocationTargetException(new CircuitException("800", "我操"));
//		} catch (Exception e) {
//			throw new EcmException(new  InvocationTargetException(e));
//		}
        ResponseClient<Map<Integer, TestArg>> rc = new ResponseClient<>();
        rc.createFromJson(
                "{\"status\":200,\"message\":\"ok\",\"dataText\":\"{\\\"20\\\":{\\\"name\\\":\\\"zxt\\\",\\\"age\\\":20},\\\"23\\\":{\\\"name\\\":\\\"cj\\\",\\\"age\\\":23}}\",\"dataType\":\"java.util.TreeMap\",\"dataElementTypes\":[\"java.lang.Integer\",\"cj.netos.openport.program.args.TestArg\"]}");
        Map<Integer, TestArg> data = rc.getData(this.getClass().getClassLoader());
        System.out.println("+++++++++" + data);
        return map;
    }

}
