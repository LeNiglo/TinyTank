<!DOCTYPE html>
<html lang="en">
<head>
	@section('head')
	<meta charset="utf-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
	<meta name="viewport" content="width=device-width, initial-scale=1" />
	<title>{{{ (isset($page_title) ? $page_title." :: " : "") }}}Tiny Tank</title>

	<!-- CSS -->
	<link href="{{ asset('/css/normalize.css') }}" rel="stylesheet" />
	<link href="{{ asset('/css/app.css') }}" rel="stylesheet" />
	<link href="{{ asset('/css/full.css') }}" rel="stylesheet" />

	<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
	<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
	<!--[if lt IE 9]>
	<script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
	<script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
	<![endif]-->

	<!-- JS -->
	<script type="text/javascript" src="{{ asset('/js/jquery-2.1.1.min.js') }}"></script>
	<script type="text/javascript" src="{{ asset('/js/bootstrap.min.js') }}"></script>

	@show
</head>
<body>

	<div class="container text-center">
		<h1 class="page-header">Tiny Tank</h1>
		<a href="/blog">Dev Blog</a>
	</div>
</body>
</html>
