1、MVP
	准备
		IView (公共的UI事件)
			showLoading()
			hideLoading()
			showError(errorMsg: String)

		IPresenter<V: IView>
			attachView(mRootView: V)
			detachView

		BasePresenter<V: IView> : IPresenter<V>
			mRootView: V
			compositeDisposable = CompositeDisposable()
			attachView(mRootView: V) {this.mRootView = mRootView}
			detachView() {
				 mRootView = null
				//保证activity结束时取消所有正在执行的订阅
				if(!compositeDisposable.isDisposed) {
					compositeDisposable.clear()
				}
			}
			addSubscription(disposable: Disposable) {
				compositeDisposable.add(disposable)
			}

	1） 定义接口
		XXContract
			View : IView   （更新UI事件）
			Presenter : IPresenter<View> (传入的泛型为此处定义的View)（请求数据事件）

	2）定义Model
		XXModel {
			//请求服务器数据
		}

	2）定义 Presenter
		XXPresenter : BasePresenter<XXContract.View>, XXContract.Presenter{
			//此处实现接口Presenter中的方法，请求数据
			//拿到数据后调用mRootView更新UI的方法，回调到页面

			mModel = XXModel()

			mRootView?.showLoading()
			mModel.getRequest(){
				showError()
				mRootView?.hideLoading()
			}

		}

	3）界面使用 (Activity或者Fragment)
		XXActivity : XXContract.View {

			//实现View中的方法，写更新UI逻辑

			val mPresenter = XXPresenter()
			//在onCreate中attachView，onDestroy中detachView
		}

