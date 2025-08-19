import styles from "./Loading.module.css";
import {TbLoaderQuarter} from "react-icons/tb";

/**
 * A reusable component that display a loading animation
 */
function Loading() {
    return <div className={styles.loadingDiv}><TbLoaderQuarter className={styles.loading}/><p>Loading ...</p></div>
}

export default Loading