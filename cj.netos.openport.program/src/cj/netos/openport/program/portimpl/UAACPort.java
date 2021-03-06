package cj.netos.openport.program.portimpl;

import cj.netos.openport.program.args.TestArg;
import cj.netos.openport.program.portface.IUAACPorts;
import cj.netos.openport.program.portface.IUCPorts;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.net.CircuitException;
import cj.studio.openport.client.Openports;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@CjService(name = "/uaac.ports")
public class UAACPort implements IUAACPorts {
    @Override
    public Map<Integer, TestArg> getAcl(List<TestArg> list, List<TestArg> set, Map<Integer, TestArg> map) throws CircuitException {

        IUCPorts iucPort = Openports.open(IUCPorts.class, "ports://openport.com/openport/uc.ports", "xx");
        TestArg arg = new TestArg();
        arg.setAge(100);
        arg.setName("tom");
        List<TestArg> test2=iucPort.test2(arg,new BigDecimal("2000.00"));
        iucPort.test2(arg,new BigDecimal("2000.00"));
        iucPort.test2(arg,new BigDecimal("2000.00"));
        iucPort.test2(arg,new BigDecimal("2000.00"));

//        iucPort.authenticate("auth.password", "netos.nettest", "cj", "11", 83283L);
//        iucPort.authenticate("auth.password", "netos.nettest", "cj", "11", 83283L);
//        iucPort.authenticate("auth.password", "netos.nettest", "cj", "11", 83283L);
//        iucPort.authenticate("auth.password", "netos.nettest", "cj", "11", 83283L);
//        iucPort.authenticate("auth.password", "netos.nettest", "cj", "11", 83283L);
//        System.out.println(iucPort.toString());

        Openports.close();
        return map;
    }
}
