package net.gongmingqm10.proficiency.api;

import java.io.Serializable;

public abstract class AbsApi<T> implements Serializable {
    public abstract ApiCallResponse<T> call();
}
