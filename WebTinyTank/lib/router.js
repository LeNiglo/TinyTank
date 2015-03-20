Router.configure({
	layoutTemplate: 'layout',
  notFoundTemplate: 'notFound',
  loadingTemplate: 'loading',
	before: function () {
		/*
		**	Check for auth here.
		**	Test if it's not affecting the speed
		*/
		this.next();
	}
});

Router.map(function() {
	this.route('home', {
		path: '/'
	});

	this.route('login');
	this.route('register');

	this.route('dev-blog');

	this.route('servers-list');

	this.route('admin', {
		path: 'admin'
	});

	this.route('admin-login', {
		path: 'admin/login'
	});

	this.route('admin-devblog', {
		path: 'admin/blog'
	});

	/* Handle 404 */
	this.route('notFound', {
		path: '*'
	});
});
