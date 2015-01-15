% CSC 488 - Compliers

% A program using non-recusive functions and procedures with and without parameters

begin
	
	% Integer function without parameters
	integer function return_ten
	begin
		return (10),
	end,

	% Integer function with parameters
	integer function add_one(integer x)
	begin
		integer y <= x + 1,
		return (y),
	end,

	% Boolean function without parameters
	boolean function alwaystrue
	begin
		return (true),
	end,

	% Boolean function with parameters
	boolean function greaterthan(integer x)
	begin
		if x > 10 then return (true), else return (false), end,
	end,

	% Function calls
	integer cat <= return_ten,
	integer dog <= return_ten(cat),
	boolean mouse <= alwaystrue,
	boolean horse <= greaterthan(cat),

	% Procedures without parameters
	procedure house
	begin 
	end,

	% Procedures with parameters
	procedure house2(integer x)
	begin
		integer house2 <= x,
		put house2, skip,
	end,

	% Procedures calls
	house,
	house2(cat),

end