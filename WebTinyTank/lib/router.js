Router.configure({
	layoutTemplate: 'layout',
	loadingTemplate: 'loading'
	// before: function () {
	// 	/*
	// 	**	Check for auth here.
	// 	**	Test if it's not affecting the speed
	// 	*/
	// 	this.next();
	// }
});

Router.map(function() {
	this.route('home', {
		path: '/'
	});

	this.route('blog');

	this.route('servers-list');

	/* Handle 404 */
	this.route('notFound', {
		path: '*'
	});
});
