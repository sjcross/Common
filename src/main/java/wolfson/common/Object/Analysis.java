//TODO: This could be an interface, rather than abstract.  See which works better when I start putting it together

package wolfson.common.Object;

import java.io.File;

/**
 * Created by sc13967 on 21/10/2016.
 *
 * Interface Analysis-type class, which will be extended by particular analyses
 *
 */
public interface Analysis {
    Results execute(File file);

}
