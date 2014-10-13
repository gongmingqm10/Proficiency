package net.gongmingqm10.proficiency.api;

import net.gongmingqm10.proficiency.model.Facts;
import net.gongmingqm10.proficiency.network.NetworkUtil;

public class CanadaFactsApi extends AbsApi<Facts> {

    public static final String URL = "http://thoughtworks-ios.herokuapp.com/facts.json";

    @Override
    public ApiCallResponse<Facts> call() {
        ApiCallResponse<Facts> response = new ApiCallResponse<Facts>(this);
        Object data = NetworkUtil.call(URL, Facts.class);
        if (data instanceof String) {
            response.setErrorMessage((String) data);
        } else {
            response.setData((Facts) data);
        }
        return response;
    }
}
