import styles from '../../style/Loading.module.css'
import loading from '../../assets/loading.svg'

function Loading() {
    return(
        <div className={styles.loading_container}>
            <img className={styles.loader} src={loading} alt='loading' />
        </div>
    )
}

export default Loading