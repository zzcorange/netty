package tools.rpc;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

/**
 * 接口
 * @author hqs
 *
 */
@WebService
@SOAPBinding(style = Style.RPC)
public interface IRPCService {
    @WebMethod
    public String RPCMethod(String str);
}