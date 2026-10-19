package twitter4j;

import java.io.IOException;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.http.HttpResponseCode;

/* JADX INFO: loaded from: classes.dex */
public class TwitterException extends Exception implements TwitterResponse, HttpResponseCode {
    private static final String[] FILTER = {"twitter4j"};
    private static final long serialVersionUID = -2623309261327598087L;
    private String errorMessage;
    private ExceptionDiagnosis exceptionDiagnosis;
    boolean nested;
    private String requestPath;
    private HttpResponse response;
    private int statusCode;

    public TwitterException(String message, Throwable cause) {
        super(message, cause);
        this.statusCode = -1;
        this.exceptionDiagnosis = null;
        this.errorMessage = null;
        this.requestPath = null;
        this.nested = false;
        decode(message);
    }

    public TwitterException(String message) {
        this(message, (Throwable) null);
    }

    public TwitterException(Exception cause) {
        this(cause.getMessage(), cause);
        if (cause instanceof TwitterException) {
            ((TwitterException) cause).setNested();
        }
    }

    public TwitterException(String message, HttpResponse res) {
        this(message);
        this.response = res;
        this.statusCode = res.getStatusCode();
    }

    public TwitterException(String message, Exception cause, int statusCode) {
        this(message, cause);
        this.statusCode = statusCode;
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        StringBuffer value = new StringBuffer();
        if (this.errorMessage != null && this.requestPath != null) {
            value.append("error - ").append(this.errorMessage).append("\n");
            value.append("request - ").append(this.requestPath).append("\n");
        } else {
            value.append(super.getMessage());
        }
        if (this.response != null) {
            return new StringBuffer().append(getCause(this.response)).append("\n").append(value.toString()).toString();
        }
        return value.toString();
    }

    private void decode(String str) {
        if (str != null && str.startsWith("{")) {
            try {
                JSONObject json = new JSONObject(str);
                if (!json.isNull("error")) {
                    this.errorMessage = json.getString("error");
                }
                if (!json.isNull("request")) {
                    this.requestPath = json.getString("request");
                }
            } catch (JSONException e) {
            }
        }
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    public String getResponseHeader(String name) {
        if (this.response == null) {
            return null;
        }
        List<String> header = this.response.getResponseHeaderFields().get(name);
        if (header.size() <= 0) {
            return null;
        }
        String value = header.get(0);
        return value;
    }

    @Override // twitter4j.TwitterResponse
    public RateLimitStatus getRateLimitStatus() {
        if (this.response == null) {
            return null;
        }
        return RateLimitStatusJSONImpl.createFromResponseHeader(this.response);
    }

    @Override // twitter4j.TwitterResponse
    public int getAccessLevel() {
        return TwitterResponseImpl.toAccessLevel(this.response);
    }

    public RateLimitStatus getFeatureSpecificRateLimitStatus() {
        if (this.response == null) {
            return null;
        }
        return RateLimitStatusJSONImpl.createFeatureSpecificRateLimitStatusFromResponseHeader(this.response);
    }

    public int getRetryAfter() {
        if (this.statusCode == 400) {
            RateLimitStatus rateLimitStatus = getRateLimitStatus();
            if (rateLimitStatus == null) {
                return -1;
            }
            int retryAfter = rateLimitStatus.getSecondsUntilReset();
            return retryAfter;
        }
        if (this.statusCode != 420) {
            return -1;
        }
        try {
            String retryAfterStr = this.response.getResponseHeader("Retry-After");
            if (retryAfterStr == null) {
                return -1;
            }
            int retryAfter2 = Integer.valueOf(retryAfterStr).intValue();
            return retryAfter2;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public boolean isCausedByNetworkIssue() {
        return getCause() instanceof IOException;
    }

    public boolean exceededRateLimitation() {
        return (this.statusCode == 400 && getRateLimitStatus() != null) || this.statusCode == 420;
    }

    public boolean resourceNotFound() {
        return this.statusCode == 404;
    }

    public String getExceptionCode() {
        return getExceptionDiagnosis().asHexString();
    }

    private ExceptionDiagnosis getExceptionDiagnosis() {
        if (this.exceptionDiagnosis == null) {
            this.exceptionDiagnosis = new ExceptionDiagnosis(this, FILTER);
        }
        return this.exceptionDiagnosis;
    }

    void setNested() {
        this.nested = true;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public String getRequestPath() {
        return this.requestPath;
    }

    public boolean isErrorMessageAvailable() {
        return this.errorMessage != null;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TwitterException)) {
            return false;
        }
        TwitterException that = (TwitterException) o;
        if (this.nested == that.nested && this.statusCode == that.statusCode) {
            if (this.errorMessage == null ? that.errorMessage != null : !this.errorMessage.equals(that.errorMessage)) {
                return false;
            }
            if (this.exceptionDiagnosis == null ? that.exceptionDiagnosis != null : !this.exceptionDiagnosis.equals(that.exceptionDiagnosis)) {
                return false;
            }
            if (this.requestPath == null ? that.requestPath != null : !this.requestPath.equals(that.requestPath)) {
                return false;
            }
            if (this.response != null) {
                if (this.response.equals(that.response)) {
                    return true;
                }
            } else if (that.response == null) {
                return true;
            }
            return false;
        }
        return false;
    }

    public int hashCode() {
        int result = this.statusCode;
        return (((((((((result * 31) + (this.exceptionDiagnosis != null ? this.exceptionDiagnosis.hashCode() : 0)) * 31) + (this.response != null ? this.response.hashCode() : 0)) * 31) + (this.errorMessage != null ? this.errorMessage.hashCode() : 0)) * 31) + (this.requestPath != null ? this.requestPath.hashCode() : 0)) * 31) + (this.nested ? 1 : 0);
    }

    @Override // java.lang.Throwable
    public String toString() {
        return new StringBuffer().append(getMessage()).append(this.nested ? "" : new StringBuffer().append("Relevant discussions can be on the Internet at:\n\thttp://www.google.co.jp/search?q=").append(getExceptionDiagnosis().getStackLineHashAsHex()).append(" or\n\thttp://www.google.co.jp/search?q=").append(getExceptionDiagnosis().getLineNumberHashAsHex()).toString()).append("\nTwitterException{").append(this.nested ? "" : new StringBuffer().append("exceptionCode=[").append(getExceptionCode()).append("], ").toString()).append("statusCode=").append(this.statusCode).append(", retryAfter=").append(getRetryAfter()).append(", rateLimitStatus=").append(getRateLimitStatus()).append(", featureSpecificRateLimitStatus=").append(getFeatureSpecificRateLimitStatus()).append(", version=").append(Version.getVersion()).append('}').toString();
    }

    private static String getCause(HttpResponse res) {
        String cause;
        int statusCode = res.getStatusCode();
        switch (statusCode) {
            case HttpResponseCode.NOT_MODIFIED /* 304 */:
                cause = "There was no new data to return.";
                break;
            case HttpResponseCode.BAD_REQUEST /* 400 */:
                cause = "The request was invalid.  An accompanying error message will explain why. This is the status code will be returned during rate limiting.";
                break;
            case HttpResponseCode.UNAUTHORIZED /* 401 */:
                cause = "Authentication credentials (http://dev.twitter.com/pages/auth) were missing or incorrect. Ensure that you have set valid conumer key/secret, access token/secret, and the system clock in in sync.";
                break;
            case HttpResponseCode.FORBIDDEN /* 403 */:
                cause = "The request is understood, but it has been refused. An accompanying error message will explain why. This code is used when requests are being denied due to update limits (http://support.twitter.com/forums/10711/entries/15364).";
                break;
            case HttpResponseCode.NOT_FOUND /* 404 */:
                cause = "The URI requested is invalid or the resource requested, such as a user, does not exist.";
                break;
            case HttpResponseCode.NOT_ACCEPTABLE /* 406 */:
                cause = "Returned by the Search API when an invalid format is specified in the request.\nReturned by the Streaming API when one or more of the parameters are not suitable for the resource. The track parameter, for example, would throw this error if:\n The track keyword is too long or too short.\n The bounding box specified is invalid.\n No predicates defined for filtered resource, for example, neither track nor follow parameter defined.\n Follow userid cannot be read.";
                break;
            case HttpResponseCode.TOO_LONG /* 413 */:
                cause = "A parameter list is too long. The track parameter, for example, would throw this error if:\n Too many track tokens specified for role; contact API team for increased access.\n Too many bounding boxes specified for role; contact API team for increased access.\n Too many follow userids specified for role; contact API team for increased access.";
                break;
            case HttpResponseCode.ENHANCE_YOUR_CLAIM /* 420 */:
                cause = "Returned by the Search and Trends API when you are being rate limited (http://dev.twitter.com/pages/rate-limiting).\nReturned by the Streaming API:\n Too many login attempts in a short period of time.\n Running too many copies of the same application authenticating with the same account name.";
                break;
            case HttpResponseCode.INTERNAL_SERVER_ERROR /* 500 */:
                cause = "Something is broken. Please post to the group (http://dev.twitter.com/pages/support) so the Twitter team can investigate.";
                break;
            case HttpResponseCode.BAD_GATEWAY /* 502 */:
                cause = "Twitter is down or being upgraded.";
                break;
            case HttpResponseCode.SERVICE_UNAVAILABLE /* 503 */:
                cause = "The Twitter servers are up, but overloaded with requests. Try again later.";
                break;
            default:
                cause = "";
                break;
        }
        return new StringBuffer().append(statusCode).append(":").append(cause).toString();
    }
}
