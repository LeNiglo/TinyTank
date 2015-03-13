<?php namespace App\Http\Controllers;
use GuzzleHttp\Client;
use Config;

class BlogController extends Controller {

	/*
	|--------------------------------------------------------------------------
	| Home Controller
	|--------------------------------------------------------------------------
	|
	| This controller renders your application's "dashboard" for users that
	| are authenticated. Of course, you are free to change or remove the
	| controller as you wish. It is just here to get your app started!
	|
	*/

	/**
	 * Create a new controller instance.
	 *
	 * @return void
	 */
	public function __construct()
	{

	}

	/**
	 * Show the application dashboard to the user.
	 *
	 * @return Response
	 */
	public function index() {



		$client = new Client([
			'base_url' => Config::get('app.api_url')
			]);

		$response = $client->get('/web/dev_blog', ['auth' => [Config::get('app.api_clientId'), Config::get('app.api_secretId')]]);


		return view('blog', array('blogs' => $response->json()['res']));
	}

}
