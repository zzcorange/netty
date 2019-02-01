package tools.rpc;

import javax.jws.WebService;

/**
 * 实现类
 * @author hqs
 *
 */
@WebService (endpointInterface = "tools.rpc.IRPCService")
public class RPCServiceImpl implements IRPCService {

    @Override
    public String RPCMethod(String str) {
        System.out.println("service received:" + str);
        return "RPC Method invoked: " + str;
    }

}